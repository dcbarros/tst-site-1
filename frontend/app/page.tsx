
import { Suspense } from "react";
import { connection } from "next/server";

type PingResponse = {
  service: string;
  status: string;
};

type ApiResult = {
  connected: boolean;
  data: PingResponse | null;
};

async function checkApi(): Promise<ApiResult> {
  const baseUrl = process.env.API_BASE_URL;

  if (!baseUrl) {
    return { connected: false, data: null };
  }

  try {
    const response = await fetch(
      `${baseUrl.replace(/\/$/, "")}/api/v1/ping`,
      {
        cache: "no-store",
        signal: AbortSignal.timeout(3000),
      }
    );

    if (!response.ok) {
      return { connected: false, data: null };
    }

    const data: unknown = await response.json();

    if (
      typeof data !== "object" ||
      data === null ||
      !("service" in data) ||
      !("status" in data) ||
      data.service !== "recipe-api" ||
      data.status !== "ok"
    ) {
      return { connected: false, data: null };
    }

    return {
      connected: true,
      data: {
        service: data.service,
        status: data.status,
      },
    };
  } catch (error) {
    console.error("Erro ao consultar API:", error);
    return { connected: false, data: null };
  }
}

async function ApiStatus() {
  await connection();

  const { connected, data } = await checkApi();

  return (
    <div className="mt-6 rounded-lg border p-6">
      <p
        role="status"
        className={
          connected
            ? "font-semibold text-green-600"
            : "font-semibold text-red-600"
        }
      >
        {connected ? "API conectada" : "API indisponível"}
      </p>

      {connected && data && (
        <div className="mt-2 text-sm text-gray-600">
          <p>Serviço: {data.service}</p>
        </div>
      )}
    </div>
  );
}

export default function Home() {
  return (
    <main className="mx-auto max-w-3xl p-8">
      <h1 className="text-3xl font-bold">
        Aplicação de Receitas
      </h1>

      <p className="mt-3">
        Verificação da comunicação entre Next.js e Spring Boot.
      </p>

      <Suspense fallback={<p>Verificando API...</p>}>
        <ApiStatus />
      </Suspense>
    </main>
  );
}
