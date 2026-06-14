export default function ProductGrid() {
  return <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">{Array.from({ length: 6 }).map((_, index) => <article key={index} className="rounded-2xl border border-slate-800 bg-slate-900 p-5 text-white">Product card placeholder</article>)}</section>;
}
