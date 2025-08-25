-- Kunci semua grant default dulu (opsional tapi rapi)
REVOKE ALL ON TABLE public.tile_version  FROM anon, authenticated;
REVOKE ALL ON TABLE public.city_master   FROM anon, authenticated;

-- Pastikan RLS aktif (idempotent – aman dijalankan berulang)
ALTER TABLE public.tile_version  ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.city_master   ENABLE ROW LEVEL SECURITY;

-- Hapus policy lama bila ada, lalu buat yang baru
DROP POLICY IF EXISTS read_tile_version_anon  ON public.tile_version;
CREATE POLICY read_tile_version_anon
  ON public.tile_version
  FOR SELECT
  TO anon
  USING (true);

DROP POLICY IF EXISTS read_city_master_auth   ON public.city_master;
CREATE POLICY read_city_master_auth
  ON public.city_master
  FOR SELECT
  TO authenticated
  USING (true);
