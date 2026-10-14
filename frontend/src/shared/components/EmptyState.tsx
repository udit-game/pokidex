import type { ReactNode } from 'react';

interface Props {
  icon?: string;
  title: string;
  message?: string;
  children?: ReactNode;
}

export function EmptyState({ icon = '🔍', title, message, children }: Props) {
  return (
    <div className="flex flex-col items-center gap-3 rounded-2xl border border-border bg-surface p-10 text-center">
      <span className="text-4xl" aria-hidden>{icon}</span>
      <h2 className="text-lg font-semibold">{title}</h2>
      {message && <p className="max-w-md text-muted">{message}</p>}
      {children}
    </div>
  );
}