import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  PointElement,
  LineElement, Filler, TimeScale
} from 'chart.js'
import {Line} from 'react-chartjs-2'
import 'chartjs-adapter-moment'
import data from '../src/data/rewardsClaimedAt.json'
import { useMantineTheme } from '@mantine/core'
import { transparentize } from 'polished'

ChartJS.register(CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Filler,
    Legend, TimeScale)

export const RewardClaim = ({min, max}) => {
  const theme = useMantineTheme()

  return (
      <Line
          options={{
            responsive: true,
            fill: true,
            plugins: {
              legend: {
                display: false
              },
            },
            scales: {
              x: {
                type: "time",
                min,
                max
              },
            },
          }}
          data={{
            labels: data.map((expiration) => expiration.date),
            datasets: [
              {
                label: 'Average points / claim',
                data: data.map((claim) => (claim.usedPoints/claim.numberOfRewardClaims)),
                yAxisID: 'y',
                borderColor: theme.colors.teal[5],
                backgroundColor: transparentize(0.5, theme.colors.teal[3]),
              },

            ],
          }}
      />
  )
}

