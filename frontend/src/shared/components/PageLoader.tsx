import { Spinner } from './Spinner';

export function PageLoader() {
  return (
    <div className="grid min-h-[40vh] place-items-center">
      <Spinner label="Loading page" />
    </div>
  );
}