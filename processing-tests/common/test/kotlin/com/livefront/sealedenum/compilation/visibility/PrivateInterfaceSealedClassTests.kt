package com.livefront.sealedenum.compilation.visibility

import com.livefront.sealedenum.testing.SealedEnumApprovalsExtension
import com.livefront.sealedenum.testing.assertApprovedGeneratedFile
import com.livefront.sealedenum.testing.assertCompiles
import com.livefront.sealedenum.testing.compile
import com.livefront.sealedenum.testing.getCommonSourceFile
import com.oneeyedmen.okeydoke.Approver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SealedEnumApprovalsExtension::class)
class PrivateInterfaceSealedClassTests {

    @Test
    fun `sealed class has correct values`() {
        assertEquals(
            listOf(
                PrivateInterfaceSealedClass.FirstObject,
                PrivateInterfaceSealedClass.SecondObject
            ),
            PrivateInterfaceSealedClass.values
        )
    }

    @Test
    fun `sealed class has correct enum values`() {
        assertEquals(
            listOf(
                PrivateInterfaceSealedClassEnum.PrivateInterfaceSealedClass_FirstObject,
                PrivateInterfaceSealedClassEnum.PrivateInterfaceSealedClass_SecondObject
            ),
            enumValues<PrivateInterfaceSealedClassEnum>().toList()
        )
    }

    @Test
    fun `sealed class has correct enum values with mapping`() {
        assertEquals(
            PrivateInterfaceSealedClass.values.map(PrivateInterfaceSealedClass::enum),
            enumValues<PrivateInterfaceSealedClassEnum>().toList()
        )
    }

    @Test
    fun `sealed class has correct enum class`() {
        assertEquals(
            PrivateInterfaceSealedClassEnum::class,
            PrivateInterfaceSealedClass.sealedEnum.enumClass
        )
    }

    @Test
    fun Approver.`compilation generates correct code`() {
        val result = compile(
            getCommonSourceFile("compilation", "visibility", "PrivateInterfaceSealedClass.kt")
        )

        assertCompiles(result)
        assertApprovedGeneratedFile("PrivateInterfaceSealedClass_SealedEnum.kt", result)
    }
}
