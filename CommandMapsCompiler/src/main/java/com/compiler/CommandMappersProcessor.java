package com.compiler;

import com.google.auto.service.AutoService;
import com.annotations.CommandsMapFactory;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import static java.util.Collections.singleton;

@AutoService(Processor.class)
public class CommandMappersProcessor extends AbstractProcessor
{

    private ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return singleton(CommandsMapFactory.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return !roundEnvironment.processingOver() &&
                new Processing(processingEnvironment).test(set, roundEnvironment);
    }
}
