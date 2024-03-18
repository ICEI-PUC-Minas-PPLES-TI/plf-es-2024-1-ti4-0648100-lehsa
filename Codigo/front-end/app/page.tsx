import Link from "next/link";
import NavBar from "./admin/NavBar";

export default function Home() {
  return (
    <main>
      <NavBar />
      <Link href='/admin'> Admin </Link>
    </main>
  );
}
