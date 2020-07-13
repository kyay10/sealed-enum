package com.livefront.sealedenum.internal

/**
 * Returns true if and only if all elements of the [Array] are unique based on the key computed by the [selector].
 *
 * This is logically equivalent to checking if [Array.distinctBy] made the array smaller, but is more efficient
 * due to short-circuiting.
 */
internal fun <T, K> Array<T>.areUniqueBy(selector: (T) -> K): Boolean {
    val set = mutableSetOf<K>()
    for (e in this) {
        if (!set.add(selector(e))) {
            return false
        }
    }
    return true
}
