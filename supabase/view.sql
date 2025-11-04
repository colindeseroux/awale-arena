create view public.view_scores as
select
  b.id as bot_id,
  b."group",
  case
    when count(g.id) = 0 then 0::numeric
    else sum(
      case
        when g.winner = 1
        and g.bot_1 = b.id
        or g.winner = 2
        and g.bot_2 = b.id then 1::numeric
        when g.winner = 0 then 0.5
        else 0::numeric
      end
    ) / count(g.id)::numeric
  end as score,
  date_trunc('day'::text, g.created_at)::date as day
from
  bots b
  left join games g on b.id = g.bot_1
  or b.id = g.bot_2
group by
  b.id,
  b."group",
  (date_trunc('day'::text, g.created_at));