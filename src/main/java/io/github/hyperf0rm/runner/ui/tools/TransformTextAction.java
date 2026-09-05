package io.github.hyperf0rm.runner.ui.tools;

import java.util.function.UnaryOperator;

public record TransformTextAction(String name, UnaryOperator<String> action) { }
