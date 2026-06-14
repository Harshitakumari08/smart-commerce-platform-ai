export function Button({ children, className = '', ...props }: React.ButtonHTMLAttributes<HTMLButtonElement>) {
  return <button className={`rounded-xl bg-cyan-400 px-4 py-2 text-slate-950 font-semibold hover:bg-cyan-300 ${className}`} {...props}>{children}</button>;
}
