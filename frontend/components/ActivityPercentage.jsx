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
import activitiesData from '../src/data/activitiesAt.json'

ChartJS.register(CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Filler,
    Legend, TimeScale)

export const sortByDate = (a, b) => {
    if (a.date < b.date) {
        return 1;
    }
    if (a.date > b.date) {
        return -1;
    }
    return 0;
}

export const ActivityPercentage = () => {
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
                labels: [...grantedPointsData.map((grantedPoints) => grantedPoints.date), ...activitiesData.map((expiredPoints) => expiredPoints.date)],
                datasets: [
                    {
                        label: 'Points Rewarded for Activity',
                        data: activitiesData.map((expiration) => expiration.rewardedPoints),
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

