import { Box, Button, Card, Group, SimpleGrid, Stack, Text, Title } from '@mantine/core'
import { ActivityPercentage } from '../../../components/ActivityPercentage'
import { ExpiredPoints } from '../../../components/ExpiredPoints'
import { GrantedPoints } from '../../../components/GrantedPoints'
import { WeeklyCheckoutsChart } from '../../../components/WeeklyCheckoutsChart'
import { RewardClaim } from '../../../components/RewardClaim'
import { DailyCheckoutChart } from '../../../components/DailyCheckoutChart'
import { Donut } from '../../../components/Donut'
import { BubbleChart } from '../../../components/Bubble'
import { DatePicker, DateRangePickerValue } from '@mantine/dates'
import { useState } from 'react'
import { ExpirationEffect } from '../../../components/ExpirationEffect'
import { BubbleChart2 } from '../../../components/Bubble2'

const ChartsPage = () => {
    const [startDate, setStartDate] = useState(undefined)
    const [endDate, setEndDate] = useState(undefined)

    return (
        <Box p={16}>
            <Stack>
                <Box sx={{ position: 'sticky', top: 0, zIndex: 1, backgroundColor: '#fff', paddingBottom: 8 }}>
                    <Group>
                        <DatePicker label="Period start" value={startDate} onChange={setStartDate} />
                        <DatePicker label="Period end" value={endDate} onChange={setEndDate} />
                    </Group>
                </Box>


                <SimpleGrid cols={2}>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Expired Points
                            </Title>
                            <ExpiredPoints min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Weekly Checkouts
                            </Title>
                            <WeeklyCheckoutsChart />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Average points / claim
                            </Title>
                            <RewardClaim min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Activity Percentage
                            </Title>
                            <ActivityPercentage min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <div>
                                <Title align="center" order={3}>
                                    Effect of expiration on daily checkouts
                                </Title>
                                <Text align="center">Vertical lines represent expirations</Text>
                            </div>
                            <ExpirationEffect min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <div>
                                <Title align="center" order={3}>
                                    Checkouts
                                </Title>
                            </div>
                            <DailyCheckoutChart min={startDate} max={endDate} />
                        </Stack>
                    </Card>

                </SimpleGrid>

                <SimpleGrid cols={4} sx={{alignItems: 'flex-start'}}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Event count by action
                            </Title>
                            <Donut min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                <Card shadow="sm" p="lg" radius="md" withBorder sx={{gridColumn: '2 / 5'}}>
                        <Stack>
                            <Title align="center" order={3}>
                                Relation between loyality and reward claims on spending
                            </Title>
                            <BubbleChart2 />
                        </Stack>
                    </Card>


                </SimpleGrid>

            </Stack>
        </Box>
    )
}

export default ChartsPage
