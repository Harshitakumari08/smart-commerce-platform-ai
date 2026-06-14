export function Card({ title, children }: { title: string; children: React.ReactNode }) {
  return <section className="rounded-2xl border border-slate-800 bg-slate-900 p-6 shadow-xl"> <h3 className="text-lg font-semibold text-white">{title}</h3> <div className="mt-4">{children}</div></section>;
}
