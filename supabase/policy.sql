create policy "public can read groups" on public.groups for
select
    to anon using (true);

create policy "public can read bots" on public.bots for
select
    to anon using (true);

create policy "public can read games" on public.games for
select
    to anon using (true);

create policy "public can read waitings" on public.waitings for
select
    to anon using (true);