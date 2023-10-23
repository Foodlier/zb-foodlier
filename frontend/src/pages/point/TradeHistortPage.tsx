import React, { useEffect } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/point/TradeHistoryPage.styled'
import axiosInstance from '../../utils/FetchCall'

const TradeHistortPage = () => {
  const pageIdx = 0
  const pageSize = 20

  const getTradeHistory = async () => {
    try {
      const res = await axiosInstance.get(
        `/api/point/transaction/${pageIdx}/${pageSize}`
      )
      console.log(res.data)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getTradeHistory()
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <S.Title>거래내역</S.Title>
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default TradeHistortPage
