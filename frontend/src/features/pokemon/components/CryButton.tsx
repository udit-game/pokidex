import { useState } from 'react';
import { Button } from '../../../shared/components/Button';
import { useLoadedPokemon } from '../context/PokemonContext';

export function CryButton() {
  const { cryUrl } = useLoadedPokemon();
  const [playing, setPlaying] = useState(false);

  if (!cryUrl) return null;

  const play = async () => {
    try {
      const audio = new Audio(cryUrl);
      audio.volume = 0.5;
      audio.onended = () => setPlaying(false);
      setPlaying(true);
      await audio.play();
    } catch {
      setPlaying(false); // autoplay blocked or format unsupported
    }
  };

  return (
    <Button variant="ghost" onClick={play} disabled={playing}>
      <span aria-hidden>🔊</span> Cry
    </Button>
  );
}