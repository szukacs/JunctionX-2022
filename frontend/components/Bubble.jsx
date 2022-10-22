import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
    PointElement,
    LineElement,
    Filler,
    TimeScale,
} from 'chart.js'
import { Bubble } from 'react-chartjs-2'
import 'chartjs-adapter-moment'
import data from '../src/data/customerLoyalty.json'
import { useMantineTheme } from '@mantine/core'
import { transparentize } from 'polished'
import moment from 'moment/moment'

if (typeof window !== 'undefined') {
    (async () => {
      const { default: zoomPlugin } = await import('chartjs-plugin-zoom');
      ChartJS.register(zoomPlugin);
    })();
  }

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Filler, Legend, TimeScale)

export const BubbleChart = () => {
    const theme = useMantineTheme()

    return (
        <Bubble
            options={{
                responsive: true,
                fill: true,
                plugins: {
                    legend: {
                        display: false
                    },
                    zoom: {
                        pan: {
                            enabled: true
                        },
                        zoom: {
                            wheel: {
                                enabled: true
                            }
                        },
                        mode: 'xy'
                      }
                },
                scales: {
                    x: {
                        title: {display: true, text: 'Loyality in days'},
                    },
                    y: {
                        title: {display: true, text: 'Rewards claimed'},
                        ticks: {
                            stepSize: 1,
                        }
                    },
                },
            }}
            data={{
                labels: data.map((expiration) => expiration.date),
                datasets: [
                    {
                        label: 'Dollars spend',
                        data: data.filter(data =>data.loyalDays > 700).map((data) => ({
                            x: data.loyalDays,
                            r: data.spending / 415,
                            y: data.rewardsClaimed,
                        })),
                        backgroundColor: theme.colors.teal[5],
                    },
                ],
            }}
        />
    )
}
