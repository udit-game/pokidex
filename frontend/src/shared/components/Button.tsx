import type { ButtonHTMLAttributes } from 'react';

type Variant = 'primary' | 'ghost';

const VARIANTS: Record<Variant, string> = {
  primary: 'bg-brand text-brand-fg hover:opacity-90',
  ghost: 'border border-border bg-surface text-fg hover:border-brand',
};

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: Variant;
}

export function Button({ variant = 'primary', className = '', ...rest }: Props) {
  return (
    <button
      type="button"
      className={`inline-flex items-center justify-center gap-2 rounded-lg px-4 py-2 text-sm font-medium transition disabled:cursor-not-allowed disabled:opacity-50 ${VARIANTS[variant]} ${className}`}
      {...rest}
    />
  );
}