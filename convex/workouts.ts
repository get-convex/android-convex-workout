import { mutation } from "./_generated/server";
import { v } from "convex/values";

export const create = mutation({
  args: {
    date: v.string(),
    duration: v.optional(v.int64()),
    activity: v.union(
      v.literal("Running"),
      v.literal("Lifting"),
      v.literal("Walking"),
      v.literal("Swimming")
    ),
  },
  handler: async (ctx, args) => {
    const identity = await ctx.auth.getUserIdentity();
    if (identity === null) {
      throw new Error("Unauthenticated call to create");
    }
    const newWorkoutId = await ctx.db.insert("workouts", {
      user: identity.email,
      date: args.date,
      duration: args.duration,
      activity: args.activity,
    });
    return newWorkoutId;
  },
});
