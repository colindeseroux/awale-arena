
create or replace function public.refresh_scores()
returns trigger as $$
begin
  delete from public.scores
  where day = date_trunc('day', now())::date;

  insert into public.scores (bot_id, "group", day, score)
  select
    bot_id,
    "group",
    day,
    score
  from public.view_scores
    where day = date_trunc('day', now())::date;

  return null;
end;
$$ language plpgsql;