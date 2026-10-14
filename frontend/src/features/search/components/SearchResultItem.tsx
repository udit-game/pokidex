
import { formatId } from '../../../shared/utils/format';
import type { PokemonSummary } from '../../pokemon/types';
import { optionId } from '../context/constants';
import { useSearch } from '../context/SearchContext';
import { Highlight } from '../../../shared/components';


interface Props {
  item: PokemonSummary;
  index: number;
}

export function SearchResultItem({ item, index }: Props) {
  const { query, activeIndex, setActiveIndex, select } = useSearch();
  const active = index === activeIndex;

  return (
    <li
      id={optionId(index)}
      role="option"
      aria-selected={active}
      onMouseEnter={() => setActiveIndex(index)}
      onClick={() => select(item.name)}
      className={`flex cursor-pointer items-center gap-3 px-4 py-2 ${active ? 'bg-bg' : ''}`}
    >
      <img
        src={item.spriteUrl}
        alt=""
        loading="lazy"
        width={40}
        height={40}
        className="h-10 w-10 [image-rendering:pixelated]"
      />
      <span className="flex-1 font-medium">
        <Highlight text={item.displayName} term={query} />
      </span>
      <span className="font-mono text-xs text-muted">{formatId(item.id)}</span>
    </li>
  );
}