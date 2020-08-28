package com.livefront.sealedenum.internal

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import javax.lang.model.element.TypeElement

internal data class SealedEnumEnumPropertySpec(
    private val sealedClass: SealedClass,
    private val parameterizedSealedClass: TypeName,
    private val sealedClassCompanionObjectElement: TypeElement,
    private val sealedEnum: ClassName,
    private val enumForSealedEnum: ClassName,
    private val enumPrefix: String
) {
    fun build(): PropertySpec {
        val propertySpecBuilder = PropertySpec.builder(pascalCaseToCamelCase(enumPrefix + "Enum"), enumForSealedEnum)
            .addOriginatingElement(sealedClassCompanionObjectElement)
            .addKdoc("The isomorphic [%T] for [this].", enumForSealedEnum)
            .receiver(parameterizedSealedClass)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return %T.sealedObjectToEnum(this)", sealedEnum)
                    .build()
            )

        return propertySpecBuilder.build()
    }
}
