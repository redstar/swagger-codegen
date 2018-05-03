package io.swagger.codegen.languages;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.models.properties.Property;

import java.util.Arrays;

abstract public class AbstractDCodegen extends DefaultCodegen implements CodegenConfig {

    public AbstractDCodegen() {
        super();

        /*
         * Reserved words.  Override this with reserved words specific to your language
         */
        setReservedWordsLowerCase(
                Arrays.asList(
                        "abstract",
                        "alias",
                        "align",
                        "asm",
                        "assert",
                        "auto",

                        "body",
                        "bool",
                        "break",
                        "byte",

                        "case",
                        "cast",
                        "catch",
                        "cdouble",
                        "cent",
                        "cfloat",
                        "char",
                        "class",
                        "const",
                        "continue",
                        "creal",

                        "dchar",
                        "debug",
                        "default",
                        "delegate",
                        "delete",
                        "deprecated",
                        "do",
                        "double",

                        "else",
                        "enum",
                        "export",
                        "extern",

                        "false",
                        "final",
                        "finally",
                        "float",
                        "for",
                        "foreach",
                        "foreach_reverse",
                        "function",

                        "goto",

                        "idouble",
                        "if",
                        "ifloat",
                        "immutable",
                        "import",
                        "in",
                        "inout",
                        "int",
                        "interface",
                        "invariant",
                        "ireal",
                        "is",

                        "lazy",
                        "long",

                        "macro",
                        "mixin",
                        "module",

                        "new",
                        "nothrow",
                        "null",

                        "out",
                        "override",

                        "package",
                        "pragma",
                        "private",
                        "protected",
                        "public",
                        "pure",

                        "real",
                        "ref",
                        "return",

                        "scope",
                        "shared",
                        "short",
                        "static",
                        "struct",
                        "super",
                        "switch",
                        "synchronized",

                        "template",
                        "this",
                        "throw",
                        "true",
                        "try",
                        "typedef",
                        "typeid",
                        "typeof",

                        "ubyte",
                        "ucent",
                        "uint",
                        "ulong",
                        "union",
                        "unittest",
                        "ushort",

                        "version",
                        "void",

                        "wchar",
                        "while",
                        "with",

                        "__FILE__",
                        "__FILE_FULL_PATH__",
                        "__MODULE__",
                        "__LINE__",
                        "__FUNCTION__",
                        "__PRETTY_FUNCTION__",

                        "__gshared",
                        "__traits",
                        "__vector",
                        "__parameters")
        );
    }

    @Override
    public String toVarName(String name) {
        if (typeMapping.keySet().contains(name) || typeMapping.values().contains(name)
                || importMapping.values().contains(name) || defaultIncludes.contains(name)
                || languageSpecificPrimitives.contains(name)) {
            return sanitizeName(name);
        }

        if (isReservedWord(name)) {
            return escapeReservedWord(name);
        }

        if (name.length() > 1) {
            return sanitizeName(Character.toLowerCase(name.charAt(0)) + name.substring(1));
        }

        return sanitizeName(name);
    }

    /**
     * Escapes a reserved word as defined in the `reservedWords` array. Handle
     * escaping those terms here. This logic is only called if a variable
     * matches the reserved words
     *
     * @return the escaped term
     */
    @Override
    public String escapeReservedWord(String name) {
        if(this.reservedWordsMappings().containsKey(name)) {
            return this.reservedWordsMappings().get(name);
        }
        return sanitizeName("_" + name);
    }

    @Override
    public String toOperationId(String operationId) {
        if (isReservedWord(operationId)) {
            LOGGER.warn(operationId + " (reserved word) cannot be used as method name. Renamed to " + escapeReservedWord(operationId));
            return escapeReservedWord(operationId);
        }
        return sanitizeName(super.toOperationId(operationId));
    }

    @Override
    public String toParamName(String name) {
        return sanitizeName(super.toParamName(name));
    }

    @Override
    public CodegenProperty fromProperty(String name, Property p) {
        CodegenProperty property = super.fromProperty(name, p);
        String nameInCamelCase = property.nameInCamelCase;
        if (nameInCamelCase.length() > 1) {
            nameInCamelCase = sanitizeName(Character.toLowerCase(nameInCamelCase.charAt(0)) + nameInCamelCase.substring(1));
        } else {
            nameInCamelCase = sanitizeName(nameInCamelCase);
        }
        property.nameInCamelCase = nameInCamelCase;
        return property;
    }
    
    /**
     * Output the Getter name for boolean property, e.g. isActive
     *
     * @param name the name of the property
     * @return getter name based on naming convention
     */
    public String toBooleanGetter(String name) {
        return "is" + getterAndSetterCapitalize(name);
    }
}
