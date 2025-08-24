-- ===== EXTENSIONS =====
create extension if not exists "uuid-ossp";
create extension if not exists "pgcrypto";

-- ===== PROFIL USER TAMBAHAN (opsional) =====
create table if not exists public.app_users (
  user_id uuid primary key references auth.users(id) on delete cascade,
  name text,
  phone text unique,
  city_code text,
  platform_default text,
  role text check (role in ('admin','super_admin','moderator','premium','trial_premium','non_premium')) default 'non_premium',
  status text check (status in ('active','suspended','banned')) default 'active',
  android_id text,
  integrity_passed_at timestamptz,
  points int default 0,
  referral_code text unique,
  referred_by text,
  premium_expired_at timestamptz,
  created_at timestamptz default now()
);

alter table public.app_users enable row level security;

create policy "app_users_select_self"
on public.app_users
for select
using (auth.uid() = user_id);

create policy "app_users_upsert_self"
on public.app_users
for insert
with check (auth.uid() = user_id);

create policy "app_users_update_self"
on public.app_users
for update
using (auth.uid() = user_id);

-- ===== MARKERS =====
create table if not exists public.markers (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  lat double precision not null,
  lng double precision not null,
  platform text not null,
  category text not null,
  city_code text,
  status text not null check (status in ('NEW','VALIDATED')) default 'NEW',
  validated_at timestamptz,
  created_at timestamptz default now(),
  expires_at timestamptz
);

create index if not exists markers_city_created_idx
  on public.markers (city_code, created_at desc);

alter table public.markers enable row level security;

-- read: semua user terautentikasi boleh baca (nanti bisa diperketat)
create policy "markers_read_auth"
on public.markers
for select
using (auth.role() = 'authenticated');

-- write: hanya pemilik (auth.uid = user_id)
create policy "markers_insert_owner"
on public.markers
for insert
with check (auth.uid() = user_id);

create policy "markers_update_owner"
on public.markers
for update
using (auth.uid() = user_id);

create policy "markers_delete_owner"
on public.markers
for delete
using (auth.uid() = user_id);

-- expiry default 48 jam
create or replace function set_marker_expiry()
returns trigger as $$
begin
  if NEW.expires_at is null then
    NEW.expires_at := now() + interval '48 hours';
  end if;
  return NEW;
end;
$$ language plpgsql;

drop trigger if exists markers_set_expiry on public.markers;
create trigger markers_set_expiry
before insert on public.markers
for each row execute function set_marker_expiry();

-- ===== HEATMAP (aggregasi) =====
create table if not exists public.heatmap_tiles (
  grid_id text primary key,
  city_code text,
  count int not null default 0,
  last_updated timestamptz default now()
);

create index if not exists heatmap_city_grid_idx
  on public.heatmap_tiles (city_code, grid_id);

alter table public.heatmap_tiles enable row level security;

create policy "heatmap_read_auth"
on public.heatmap_tiles
for select
using (auth.role() = 'authenticated');

-- ===== TILE VERSION (kontrol cache klien) =====
create table if not exists public.tile_version (
  version text primary key,
  updated_at timestamptz default now()
);

-- ===== MASTER KOTA (seed nanti) =====
create table if not exists public.city_master (
  code text primary key,
  name text not null
);
