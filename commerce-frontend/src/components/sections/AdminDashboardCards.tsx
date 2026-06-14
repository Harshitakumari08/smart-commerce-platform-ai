export default function AdminDashboardCards() {
  return <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-5">{['Revenue','Orders','Users','Products','Inventory'].map((label) => <article key={label} className="rounded-2xl border border-slate-800 bg-slate-900 p-6 text-white">{label}</article>)}</section>;
}
