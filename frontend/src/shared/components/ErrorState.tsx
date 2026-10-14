import { Button } from './Button';

interface Props {
  title?: string;
  message: string;
  onRetry?: () => void;
}

export function ErrorState({ title = 'Something went wrong', message, onRetry }: Props) {
  return (
    <div
      role="alert"
      className="flex flex-col items-center gap-3 rounded-2xl border border-border bg-surface p-8 text-center"
    >
      <span className="text-4xl" aria-hidden>⚠️</span>
      <h2 className="text-lg font-semibold">{title}</h2>
      <p className="max-w-md text-muted">{message}</p>
      {onRetry && <Button onClick={onRetry}>Try again</Button>}
    </div>
  );
}