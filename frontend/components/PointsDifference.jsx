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
import grantedPointsData from '../src/data/pointsGrantedAt.json'
import expiredPointsData from '../src/data/pointsExpireAt.json'

ChartJS.register(CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Filler,
    Legend, TimeScale)

export const PointsDifference = () => {
    return (
        <Line
            options={{
                responsive: true,
                fill: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Chart.js Bar Chart',
                    },
                },
                scales: {
                    x: {
                        type: "time"
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
                labels: [...grantedPointsData.map((grantedPoints) => grantedPoints.date), ...expiredPointsData.map((expiredPoints) => expiredPoints.date)],
                datasets: [
                    {
                        label: 'Expired Points',
                        data: expiredPointsData.map((expiration) => expiration.expiredPoints),
                        yAxisID: 'y'
                    },
                    {
                        label: 'Granted Points',
                        data: grantedPointsData.map((expiration) => expiration.grantedPoints),
                        yAxisID: 'y1'
                    },
                ],
            }}
        />
    )
}
