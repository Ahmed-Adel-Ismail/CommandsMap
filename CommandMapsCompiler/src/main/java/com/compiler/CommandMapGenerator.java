package com.compiler;

import com.annotations.Command;
import com.mapper.CommandsMap;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.annotations.Command.NULL_DOUBLE;
import static com.annotations.Command.NULL_FLOAT;
import static com.annotations.Command.NULL_INTEGER;
import static com.annotations.Command.NULL_LONG;
import static com.annotations.Command.NULL_STRING;

/**
 * working on the example in this link :
 * <p>
 * http://hannesdorfmann.com/annotation-processing/annotationprocessing101
 * <p>
 * in the class :
 * <p>
 * public class FactoryGroupedClasses {
 * <p>
 * Created by Ahmed Adel Ismail on 7/16/2017.
 */

class CommandMapGenerator implements Consumer<TypeElement> {

    private static final String INDENT = "    ";
    private static final String NEW_LINE = "\n";
    private static final String END_LINE = ";\n";
    private static final String START_BLOCK = "{\n";
    private static final String END_BLOCK = "}\n";
    private static final String END_METHOD_CALL = ");\n";

    private final ProcessingEnvironment environment;


    CommandMapGenerator(ProcessingEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void accept(@NonNull TypeElement element) throws Exception {
        String generatedClassName = originalClassName(element) + CommandsMap.GENERATED_NAME;
        try {
            generateFile(generatedClassName, generateCode(element, generatedClassName));
        } catch (IOException e) {
            // this occurs if the file already existing after its first run, this is normal
        } catch (Exception e) {
            environment.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private String originalClassName(@NonNull TypeElement element) {
        return element.getSimpleName().toString();
    }

    private void generateFile(String generatedClassName, StringBuilder stringBuilder)
            throws IOException {
        JavaFileObject source = environment.getFiler().createSourceFile(generatedClassName);
        Writer writer = source.openWriter();
        writer.write(stringBuilder.toString());
        writer.flush();
        writer.close();
    }

    private StringBuilder generateCode(@NonNull TypeElement element, String generatedClassName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ")
                .append(packageName(element))
                .append(END_LINE)
                .append(NEW_LINE);

        stringBuilder.append("import com.mapper.*")
                .append(END_LINE)
                .append(NEW_LINE);

        stringBuilder.append("public final class ")
                .append(generatedClassName)
                .append(" extends CommandsMap ")
                .append(START_BLOCK)
                .append(NEW_LINE);

        stringBuilder
                .append(INDENT)
                .append("public ")
                .append(generatedClassName)
                .append("()")
                .append(START_BLOCK)
                .append(NEW_LINE);

        Observable.fromIterable(element.getEnclosedElements())
                .filter(byAnnotatedMethods())
                .blockingForEach(addCommand(stringBuilder, originalClassName(element)));

        stringBuilder.append(INDENT)
                .append(END_BLOCK)
                .append(END_BLOCK)
                .append(NEW_LINE);

        return stringBuilder;
    }

    private String packageName(@NonNull TypeElement element) {
        return environment.getElementUtils().getPackageOf(element).getQualifiedName().toString();
    }


    private Predicate<Element> byAnnotatedMethods() {
        return new Predicate<Element>() {
            @Override
            public boolean test(@NonNull Element element) throws Exception {
                return element.getAnnotation(Command.class) != null;
            }
        };
    }

    private Consumer<? super Element> addCommand(final StringBuilder stringBuilder,
                                                 final String className) {
        return new Consumer<Element>() {
            @Override
            public void accept(@NonNull Element element) throws Exception {

                ExecutableElement method = (ExecutableElement) element;
                List<?> parameters = method.getParameters();

                if (parameters == null || parameters.isEmpty()) {
                    addCommandBody(element, stringBuilder, zeroParameterBody(method, className));
                } else {
                    int parametersCount = parameters.size();
                    if (parametersCount == 1) {
                        addCommandBody(element, stringBuilder, oneParameterBody(method, className));
                    } else if (parametersCount == 2) {
                        addBiCommandBody(element, stringBuilder, twoParameterBody(method, className));
                    }
                }
            }
        };
    }

    private void addCommandBody(@NonNull Element element,
                                StringBuilder stringBuilder,
                                String commandBody) {
        
        stringBuilder
                .append(INDENT)
                .append(INDENT)
                .append("addCommand(")
                .append(parseKeyFromCommandAnnotation(element))
                .append(", ")
                .append("new Command(){")
                .append(NEW_LINE)
                .append(INDENT)
                .append(INDENT)
                .append(INDENT)
                .append("public void accept(Object o1){")
                .append(NEW_LINE)
                .append(commandBody)
                .append(INDENT)
                .append(INDENT)
                .append(INDENT)
                .append("}")
                .append(NEW_LINE)
                .append(INDENT)
                .append(INDENT)
                .append("}")
                .append(END_METHOD_CALL)
                .append(NEW_LINE);
    }

    private String zeroParameterBody(ExecutableElement element, String className) {
        Name methodName = element.getSimpleName();
        return bodyPrefix(methodName, className) + "()" + END_LINE + bodyPostfix(methodName, className);
    }

    private String oneParameterBody(ExecutableElement element, String className) {
        Name methodName = element.getSimpleName();
        String typeCast = element.getParameters().get(0).asType().toString();
        return bodyPrefix(methodName, className) + "((" + typeCast + ")o1)" + END_LINE +
                bodyPostfix(methodName, className);
    }

    private void addBiCommandBody(Element element,
                                  StringBuilder stringBuilder,
                                  String commandBody) {

        stringBuilder.append(INDENT)
                .append(INDENT)
                .append("addBiCommand(")
                .append(parseKeyFromCommandAnnotation(element))
                .append(", ")
                .append("new BiCommand(){")
                .append(NEW_LINE)
                .append(INDENT)
                .append(INDENT)
                .append(INDENT)
                .append("public void accept(Object o1, Object o2){")
                .append(NEW_LINE)
                .append(commandBody)
                .append(INDENT)
                .append(INDENT)
                .append(INDENT)
                .append("}")
                .append(NEW_LINE)
                .append(INDENT)
                .append(INDENT)
                .append("}")
                .append(END_METHOD_CALL)
                .append(NEW_LINE);
    }

    private String twoParameterBody(ExecutableElement element, String className) {
        Name methodName = element.getSimpleName();
        String typeCastOne = element.getParameters().get(0).asType().toString();
        String typeCastTwo = element.getParameters().get(1).asType().toString();
        return bodyPrefix(methodName, className)
                + "((" + typeCastOne + ")o1, (" + typeCastTwo + ")o2)" + END_LINE +
                bodyPostfix(methodName, className);
    }

    private Object parseKeyFromCommandAnnotation(@NonNull Element element) {

        Command annotation = element.getAnnotation(Command.class);

        Object key = annotation.value();
        if (!key.equals(NULL_INTEGER)) {
            return key;
        }

        key = annotation.keyString();
        if (!key.equals(NULL_STRING)) {
            return "\"" + key + "\"";
        }

        key = annotation.keyLong();
        if (!key.equals(NULL_LONG)) {
            return key + "L";
        }

        key = annotation.keyDouble();
        if (!key.equals(NULL_DOUBLE)) {
            return key + "D";
        }

        key = annotation.keyFloat();
        if (!key.equals(NULL_FLOAT)) {
            return key + "F";
        }

        throw new IllegalArgumentException("@" + Command.class.getSimpleName()
                + " must have a value in : " + element.getSimpleName() + "()");
    }

    private String bodyPrefix(Name methodName, String className) {
        return INDENT + INDENT + INDENT + INDENT +
                "if(getHostObject() != null){"
                + NEW_LINE
                + INDENT + INDENT + INDENT + INDENT + INDENT
                + "((" + className + ")getHostObject())." + methodName;
    }

    private String bodyPostfix(Name methodName, String className) {
        return INDENT + INDENT + INDENT + INDENT +
                "} else { "
                + NEW_LINE
                + INDENT + INDENT + INDENT + INDENT + INDENT
                + "java.lang.System.out.println(\" " + CommandsMap.class.getSimpleName() + " cleared for "
                + className + "." + methodName + "() \"); "
                + NEW_LINE
                + INDENT + INDENT + INDENT + INDENT
                + "}"
                + NEW_LINE;
    }


}
