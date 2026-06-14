export default function EmptyState({ title, message }: { title: string; message: string }) {
  return <article className="rounded-2xl border border-dashed border-slate-700 bg-slate-900 p-8 text-center text-slate-300"> <h3 className="text-xl font-semibold text-white">{title}</h3> <p className="mt-2">{message}</p></article>;
}
