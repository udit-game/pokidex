/** Bolds the first occurrence of `term` inside `text`. */
export function Highlight({ text, term }: { text: string; term: string }) {
  const needle = term.trim().toLowerCase();
  const index = needle ? text.toLowerCase().indexOf(needle) : -1;
  if (index < 0) return <>{text}</>;

  const end = index + needle.length;
  return (
    <>
      {text.slice(0, index)}
      <mark className="bg-transparent font-bold text-brand">{text.slice(index, end)}</mark>
      {text.slice(end)}
    </>
  );
}