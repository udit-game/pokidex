import type { ReactNode } from 'react';

interface Props {
  title?: string;
  className?: string;
  children: ReactNode;
}

export function Card({ title, className = '', children }: Props) {
  return (
    <section className={`rounded-2xl border border-border bg-surface p-5 ${className}`}>
      {title && (
        <h2 className="mb-4 text-xs font-semibold uppercase tracking-wider text-muted">{title}</h2>
      )}
      {children}
    </section>
  );
}