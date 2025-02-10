package io.nearpay.terminalsdk.data
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any, S : Any> mapProperties(source: S): T {
    val targetConstructor = T::class.primaryConstructor
        ?: throw IllegalArgumentException("Target class must have a primary constructor")

    val sourceProperties = source::class.memberProperties
        .filterIsInstance<KProperty1<S, *>>() // Only take properties compatible with the source type
        .associateBy { it.name }

    val args = targetConstructor.parameters.associateWith { parameter ->
        val sourceProperty = sourceProperties[parameter.name]
        val sourceValue = sourceProperty?.get(source)  // This will be safely typed now

        if (sourceValue != null && parameter.type.classifier is KClass<*>) {
            val targetClass = parameter.type.classifier as KClass<*>

            // Recursively call mapProperties for nested classes if both source and target are data classes
            if (targetClass.isData && sourceValue::class.isData) {
                mapProperties(targetClass, sourceValue)
            } else {
                sourceValue
            }
        } else {
            sourceValue
        }
    }

    return targetConstructor.callBy(args).also { target ->
        T::class.memberProperties.forEach { targetProperty ->
            if (targetProperty is KMutableProperty<*>) {
                sourceProperties[targetProperty.name]?.let { sourceProperty ->
                    val sourceValue = sourceProperty.get(source)  // Safely typed access

                    if (sourceValue != null && targetProperty.returnType.classifier is KClass<*>) {
                        val targetClass = targetProperty.returnType.classifier as KClass<*>

                        if (targetClass.isData && sourceValue::class.isData) {
                            val nestedValue = mapProperties(targetClass, sourceValue)
                            targetProperty.setter.call(target, nestedValue)
                        } else {
                            targetProperty.setter.call(target, sourceValue)
                        }
                    } else {
                        targetProperty.setter.call(target, sourceValue)
                    }
                }
            }
        }
    }
}

// Helper function to handle recursive calls with type erasure
fun <T : Any> mapProperties(targetClass: KClass<T>, source: Any): T {
    val sourceProperties = source::class.memberProperties
        .filterIsInstance<KProperty1<Any, *>>()
        .associateBy { it.name }

    return targetClass.primaryConstructor?.let { constructor ->
        val args = constructor.parameters.associateWith { parameter ->
            val sourceProperty = sourceProperties[parameter.name]
            val sourceValue = sourceProperty?.get(source)

            if (sourceValue != null && parameter.type.classifier is KClass<*>) {
                val nestedTargetClass = parameter.type.classifier as KClass<*>

                if (nestedTargetClass.isData && sourceValue::class.isData) {
                    mapProperties(nestedTargetClass, sourceValue)
                } else {
                    sourceValue
                }
            } else {
                sourceValue
            }
        }

        constructor.callBy(args)
    } ?: throw IllegalArgumentException("Target class must have a primary constructor")
}
