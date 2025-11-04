create trigger refresh_scores_on_games after insert
or
update
or delete on public.games for each statement execute function public.refresh_scores ();

create trigger refresh_scores_on_bots after insert
or
update
or delete on public.bots for each statement execute function public.refresh_scores ();