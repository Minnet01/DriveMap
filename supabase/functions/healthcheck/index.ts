// supabase/functions/healthcheck/index.ts
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

Deno.serve(async (_req) => {
  const url = Deno.env.get("SUPABASE_URL")!;
  const serviceKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;

  try {
    const supabase = createClient(url, serviceKey, { auth: { persistSession: false } });

    // panggilan ringan, tidak mengekspose data sensitif
    const { error } = await supabase.auth.admin.listUsers({ page: 1, perPage: 1 });
    if (error) throw error;

    return new Response(JSON.stringify({
      ok: true,
      ref: new URL(url).host,
      active_connections: 0,
      ts: new Date().toISOString(),
    }), { headers: { "Content-Type": "application/json" } });
  } catch (e) {
    return new Response(JSON.stringify({ ok: false, error: String(e) }), {
      headers: { "Content-Type": "application/json" },
      status: 500,
    });
  }
});
