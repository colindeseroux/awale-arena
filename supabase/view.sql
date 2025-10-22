create view public.daily_bot_scores
with (security_invoker = on) as
select 
    b.id as bot_id,
    b."group",
    case 
        when COUNT(g.id) = 0 THEN 0
        else (
            SUM(
                case 
                    when (g.winner = 1 AND g.bot_1 = b.id) OR (g.winner = 2 AND g.bot_2 = b.id) THEN 1
                    when g.winner = 0 THEN 0.5
                    else 0
                end
            )::numeric / COUNT(g.id)
        )
    end as score
from bots b
left join games g 
    on b.id = g.bot_1 OR b.id = g.bot_2
where g.created_at >= date_trunc('day', now() - interval '1 day')
group by b.id, b."group";
