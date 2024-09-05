import { mutation, query } from "./_generated/server";
import { v } from "convex/values";

export const store = mutation({
  args: {
    date: v.string(),
    duration: v.optional(v.number()),
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

export const get = query({
    args: {},
    handler: async (ctx, _) => {
        const identity = await ctx.auth.getUserIdentity();
    if (identity === null) {
      throw new Error("Unauthenticated call to create");
    }
      const workouts = await ctx.db
        .query("workouts")
        .filter((q) => q.eq(q.field("user"), identity.email))
        .order("desc")
        .take(100);
      return workouts;
    },
  });
