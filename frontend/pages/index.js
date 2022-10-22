import Head from 'next/head'
import Image from 'next/image'
import styles from '../styles/Home.module.css'
import Link from "next/link";
import PointsDifferencePage from "./web/points-difference";

export default function Home() {
  return (
    <div className={styles.container}>
      <Head>
        <title>Create Next App</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className={styles.main}>
        <Link href={"/web/charts"}>
          All Charts
        </Link>
        <Link href={"/web/weekly-checkouts"}>
          Weekly Checkouts Chart
        </Link>
        <Link href={"/web/expired-points"}>
            Expired Points
        </Link>
        <Link href={"/web/granted-points"}>
            Granted Points
        </Link>
          <Link href={"/web/points-difference"}>
              Points Difference
          </Link>
          <Link href={"/web/activity-percentage"}>
              Activity percentage
          </Link>
          <Link href={"/web/reward-claim"}>
              Reward Claimed
          </Link>
          <Link href={"/web/expiration-effect"}>
              Expiration effect
          </Link>
      </main>
        PointsDifferencePage

      <footer className={styles.footer}>
        <a
          href="https://vercel.com?utm_source=create-next-app&utm_medium=default-template&utm_campaign=create-next-app"
          target="_blank"
          rel="noopener noreferrer"
        >
          Powered by{' '}
          <span className={styles.logo}>
            <Image src="/vercel.svg" alt="Vercel Logo" width={72} height={16} />
          </span>
        </a>
      </footer>
    </div>
  )
}
