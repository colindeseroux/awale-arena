update bots
set activated = false
where id not in (
    select bot_id
    from (
        select
            bot_id,
            "group",
            rank() over (
                partition by "group"
                order by score desc
            ) as rnk
        from view_scores
        where day = date_trunc('day', now() - interval '1 day')::date
    ) ranked
    where rnk = 1
)
and id in (
    select bot_id
    from view_scores
    where day = date_trunc('day', now() - interval '1 day')::date
);
