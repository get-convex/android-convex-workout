package dev.convex.workouttracker.models

import kotlinx.serialization.Serializable

/**
 * Contains the details of a workout that can be stored on the Convex backend for the app.
 */
@Serializable
data class Workout(val date: String, val activity: Activity, val duration: Int? = null) {

    /**
     * Converts this [Workout] to a map for easily sending as `args` to the Convex backend.
     */
    fun toArgs(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["date"] = date
        map["activity"] = activity.toString()
        duration?.let {
            map["duration"] = duration.toUInt()
        }
        return map
    }

    @Serializable
    enum class Activity {
        Unknown,
        Running,
        Walking,
        Swimming,
        Lifting;

        override fun toString(): String {
            return when (this) {
                Unknown -> throw IllegalArgumentException()
                else -> super.toString()
            }
        }

        companion object {
            /***
             * All [Activity] options that are valid for use in the app.
             */
            val options get() = entries.filter { e -> e != Unknown }
        }
    }
}