import Link from "next/link";

export default function Home() {
  return (
    <main>
      <p>Hello, World!</p>
      <Link href='/admin'> Admin </Link>
    </main>
  );
}
