import { useState } from 'react';

/** Mount with key={src} so the failed state resets when the image changes. */
export function Artwork({ src, alt }: { src: string; alt: string }) {
  const [failed, setFailed] = useState(false);

  if (failed) {
    return (
      <div
        role="img"
        aria-label={alt}
        className="grid h-64 w-64 place-items-center rounded-full bg-bg text-6xl text-muted md:h-80 md:w-80"
      >
        ?
      </div>
    );
  }

  return (
    <img
      src={src}
      alt={alt}
      onError={() => setFailed(true)}
      className="h-64 w-64 object-contain drop-shadow-2xl md:h-80 md:w-80"
    />
  );
}