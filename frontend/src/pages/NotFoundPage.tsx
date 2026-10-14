import { Link } from 'react-router-dom';
import { EmptyState } from '../shared/components';


export default function NotFoundPage() {
  return (
    <EmptyState icon="🗺️" title="Page not found" message="That page doesn't exist.">
      <Link to="/" className="font-medium text-brand hover:underline">
        Go home
      </Link>
    </EmptyState>
  );
}