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
import data from '../src/data/pointsGrantedAt.json'
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

export const GrantedPoints = ({min, max}) => {
  const theme = useMantineTheme()

  return (
      <Line
          options={{
            responsive: true,
            fill: true,
            plugins: {
              legend: {
                position: 'top',
              },
            },
            scales: {
              x: {
                type: "time",
                min,
                max
              },
              y: {
                type: 'linear',
                display: true,
                position: 'left',
              },
              y1: {
                type: 'linear',
                display: true,
                position: 'right',
                grid: {
                  drawOnChartArea: false,
                },
              },
            },
          }}
          data={{
            labels: data.map((expiration) => expiration.date),
            datasets: [
              {
                label: 'Number Of Grants',
                data: data.map((expiration) => expiration.numberOfPointGrants),
                yAxisID: 'y',
                borderColor: theme.colors.indigo[5],
                backgroundColor: transparentize(0.5, theme.colors.indigo[3])
              },
              {
                label: 'Granted Points',
                data: data.map((expiration) => expiration.grantedPoints),
                yAxisID: 'y1',
                borderColor: theme.colors.teal[5],
                backgroundColor: transparentize(0.5, theme.colors.teal[3])
              },
            ],
          }}
      />
  )
}