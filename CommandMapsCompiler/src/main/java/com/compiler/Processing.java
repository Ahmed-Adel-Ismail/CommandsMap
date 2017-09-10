package com.compiler;

import com.annotations.CommandsMapFactory;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * a class that handles processing annotations
 * <p>
 * Created by Ahmed Adel Ismail on 6/25/2017.
 */
class Processing implements BiPredicate<Set<? extends TypeElement>, RoundEnvironment> {

    private final ProcessingEnvironment environment;


    Processing(ProcessingEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public boolean test(Set<? extends TypeElement> typeElements, RoundEnvironment round) {
        Set<? extends Element> elements = round.getElementsAnnotatedWith(CommandsMapFactory.class);
        if (elements == null) elements = new HashSet<>();

        Observable.fromIterable(elements)
                .filter(byClassElement())
                .map(toTypeElement())
                .subscribe(new CommandMapGenerator(environment), printErrorInMessager(environment));
        return false;
    }


    private static Predicate<Element> byClassElement() {
        return new Predicate<Element>() {
            @Override
            public boolean test(@NonNull Element element) throws Exception {
                return element.getKind().equals(ElementKind.CLASS);
            }
        };
    }

    private static Function<Element, TypeElement> toTypeElement() {
        return new Function<Element, TypeElement>() {
            @Override
            public TypeElement apply(@NonNull Element element) throws Exception {
                return TypeElement.class.cast(element);
            }
        };
    }


    private static Consumer<Throwable> printErrorInMessager(final ProcessingEnvironment environment) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                environment.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, throwable.getMessage());
            }
        };
    }


}
