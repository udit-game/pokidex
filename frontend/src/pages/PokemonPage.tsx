import { useParams } from 'react-router-dom';
import { PokemonProvider } from '../features/pokemon/context/PokemonProvider';
import { PokemonView } from '../features/pokemon/components/PokemonView';


export default function PokemonPage() {
  const { name = '' } = useParams<{ name: string }>();

  return (
    <PokemonProvider key={name} name={name}>
      <PokemonView />
    </PokemonProvider>
  );
}