package com.livefront.sealedenum

/**
 * Indicates that an implementation of [SealedEnum] should be generated for the annotated sealed class.
 *
 * For example,
 * ```
 * @GenSealedEnum
 * sealed class Alpha {
 *     object Beta : Alpha()
 *     object Gamma : Alpha()
 * }
 * ```
 * will generate the following object:
 * ```
 * object AlphaSealedEnum : SealedEnum<Alpha> {
 *     override val values: List<Alpha> = listOf(
 *         Alpha.Beta,
 *         Alpha.Gamma
 *     )
 *
 *     override fun ordinalOf(obj: Alpha): Int = when (obj) {
 *         Alpha.Beta -> 0
 *         Alpha.Gamma -> 1
 *     }
 *
 *     override fun nameOf(obj: AlphaSealedEnum): String = when (obj) {
 *         Alpha.Beta -> "Alpha_Beta"
 *         Alpha.Gamma -> "Alpha_Gamma"
 *     }
 *
 *     override fun valueOf(name: String): AlphaSealedEnum = when (name) {
 *         "Alpha_Beta" -> Alpha.Beta
 *         "Alpha_Gamma" -> Alpha.Gamma
 *         else -> throw IllegalArgumentException("""No sealed enum constant $name""")
 *     }
 * }
 * ```
 *
 * The [SealedEnum.valueOf] and [SealedEnum.nameOf] methods mimic the corresponding [Enum] methods, and use
 * the simple names of the objects joined by underscores.
 *
 * If there is a nested hierarchy of sealed classes, then `values` will contain all object subclasses, with the
 * tree ordering given by [traversalOrder]. By default, an [TreeTraversalOrder.IN_ORDER] will be used.
 *
 * If multiple ordering strategies are desired simultaneously, then the sealed class can be annotated with multiple
 * [GenSealedEnum] annotations, with different [TreeTraversalOrder]s. This will cause multiple objects to be generated,
 * with different names to indicate the traversal order used.
 *
 * For example,
 * ```
 * @GenSealedEnum(traversalOrder = TreeTraversalOrder.IN_ORDER)
 * @GenSealedEnum(traversalOrder = TreeTraversalOrder.LEVEL_ORDER)
 * sealed class Alpha {
 *     sealed class Beta : Alpha() {
 *         object Gamma : Beta()
 *     }
 *     object Delta : Alpha()
 *     sealed class Epsilon : Alpha() {
 *         object Zeta : Epsilon()
 *     }
 * }
 * ```
 * will generate two objects:
 * ```
 * object AlphaLevelOrderSealedEnum : SealedEnum<Alpha> {
 *     override val values: List<Alpha> = listOf(
 *         Alpha.Delta,
 *         Alpha.Beta.Gamma,
 *         Alpha.Epsilon.Zeta
 *     )
 *
 *     override fun ordinalOf(obj: Alpha): Int = when (obj) {
 *         Alpha.Delta -> 0
 *         Alpha.Beta.Gamma -> 1
 *         Alpha.Epsilon.Zeta -> 2
 *     }
 *
 *     override fun nameOf(obj: AlphaLevelOrderSealedEnum): String = when (obj) {
 *         Alpha.Delta -> "Alpha_Delta"
 *         Alpha.Beta.Gamma -> "Alpha_Beta_Gamma"
 *         Alpha.Epsilon.Zeta -> "Alpha_Epsilon_Zeta"
 *     }
 *
 *     override fun valueOf(name: String): AlphaLevelOrderSealedEnum = when (name) {
 *         "Alpha_Delta" -> Alpha.Delta
 *         "Alpha_Beta_Gamma" -> Alpha.Beta.Gamma
 *         "Alpha_Epsilon_Zeta" -> Alpha.Epsilon.Zeta
 *         else -> throw IllegalArgumentException("""No sealed enum constant $name""")
 *     }
 * }
 *
 * object AlphaInOrderSealedEnum : SealedEnum<Alpha> {
 *     override val values: List<Alpha> = listOf(
 *         Alpha.Beta.Gamma,
 *         Alpha.Delta,
 *         Alpha.Epsilon.Zeta
 *     )
 *
 *     override fun ordinalOf(obj: Alpha): Int = when (obj) {
 *         Alpha.Beta.Gamma -> 0
 *         Alpha.Delta -> 1
 *         Alpha.Epsilon.Zeta -> 2
 *     }
 *
 *     override fun nameOf(obj: AlphaInOrderSealedEnum): String = when (obj) {
 *         Alpha.Beta.Gamma -> "Alpha_Beta_Gamma"
 *         Alpha.Delta -> "Alpha_Delta"
 *         Alpha.Epsilon.Zeta -> "Alpha_Epsilon_Zeta"
 *     }
 *
 *     override fun valueOf(name: String): AlphaInOrderSealedEnum = when (name) {
 *         "Alpha_Beta_Gamma" -> Alpha.Beta.Gamma
 *         "Alpha_Delta" -> Alpha.Delta
 *         "Alpha_Epsilon_Zeta" -> Alpha.Epsilon.Zeta
 *         else -> throw IllegalArgumentException("""No sealed enum constant $name""")
 *     }
 * }
 * ```
 *
 * If the sealed class (or any child sealed class) has non-object and non-sealed subclasses, they will cause
 * annotation processing to fail.
 *
 * If the sealed class has generic type arguments, then the generated object will specify them if they are invariant,
 * otherwise the sealed class type will be star-projected.
 *
 * If [generateEnum] is true, then a normal enum class will be generated that is close to equivalent to the original
 * sealed class. In essence, the enum will implement all interfaces that the sealed class implements, and the objects
 * will be isomorphic to the sealed class objects. To work with the enum, the generated object will implement
 * [EnumForSealedEnumProvider] in addition to [SealedEnum]. This provider has [EnumForSealedEnumProvider.enumClass],
 * which returns the enum's class, and [EnumForSealedEnumProvider.sealedObjectToEnum] and
 * [EnumForSealedEnumProvider.enumToSealedObject] together provide the isomorphism from the sealed objects to
 * enum constants.
 *
 * In addition, the ordinals of the enum constants will match the ordinals for the sealed class objects (as
 * determined by [traversalOrder]), and the names of the sealed objects (as given by [SealedEnum.nameOf] and
 * [SealedEnum.valueOf]) will match the names of the enum constants (assuming no obfuscation occurs).
 */
@Suppress("DEPRECATED_JAVA_ANNOTATION")
@java.lang.annotation.Repeatable(GenSealedEnums::class)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class GenSealedEnum(
    val traversalOrder: TreeTraversalOrder = TreeTraversalOrder.IN_ORDER,
    val generateEnum: Boolean = false
)

/**
 * This is the container annotation for multiple [GenSealedEnum] annotations.
 */
annotation class GenSealedEnums(vararg val value: GenSealedEnum)
