import { defineSchema, defineTable } from "convex/server";
import { v } from "convex/values";

export default defineSchema({
  workouts: defineTable({
    activity: v.string(),
    date: v.string(),
    duration: v.optional(v.float64()),
    userId: v.string(),
  }).index("userId_date", ["userId", "date"]),
});
