update bots
set
  activated = false
where
  id not in (
    select
      bot_id
    from
      (
        select
          bot_id,
          "group",
          rank() over (
            partition by
              "group"
            order by
              score desc
          ) as rnk
        from
          daily_bot_scores
      ) ranked
    where
      rnk <= 1
  )
  and id in (
    select
      bot_id
    from
      daily_bot_scores
  );