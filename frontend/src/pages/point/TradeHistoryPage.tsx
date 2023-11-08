import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/point/TradeHistoryPage.styled'
import axiosInstance from '../../utils/FetchCall'

interface HistoryType {
  changePoint: number
  currentPoint: number
  description: string
  sender: string
  transactionAt: string
  requestId: number
}

const TradeHistoryPage = () => {
  const [history, setHistory] = useState<HistoryType[]>([])
  const navigate = useNavigate()

  const getTradeHistory = async () => {
    const pageIdx = 0
    const pageSize = 20
    try {
      const res = await axiosInstance.get(
        `/api/point/transaction/${pageIdx}/${pageSize}`
      )
      setHistory(res.data.content)
    } catch (error) {
      console.log(error)
    }
  }

  const goToReview = (name: string, requestId: number) => {
    const body = {
      requestId,
      name,
    }
    navigate('/refrigerator/request/review', { state: body })
  }

  useEffect(() => {
    getTradeHistory()
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <S.Title>거래내역</S.Title>
        {history.length > 0 ? (
          history.map(el => (
            <S.HistoryWrap key={el.transactionAt}>
              <S.HistoryLeftWrap>
                <S.HistoryPartWrap>
                  <S.HistoryPartTitle>거래 일시</S.HistoryPartTitle>
                  <S.HistoryPartDes>{el.transactionAt}</S.HistoryPartDes>
                </S.HistoryPartWrap>
                <S.HistoryPartWrap>
                  <S.HistoryPartTitle>{el.description}</S.HistoryPartTitle>
                  <S.HistoryPartDes>{el.changePoint}</S.HistoryPartDes>
                </S.HistoryPartWrap>
                <S.HistoryPartWrap>
                  <S.HistoryPartTitle>상대방 닉네임</S.HistoryPartTitle>
                  <S.HistoryPartDes>{el.sender}</S.HistoryPartDes>
                </S.HistoryPartWrap>
                <S.HistoryPartWrap>
                  <S.HistoryPartTitle>이후 포인트</S.HistoryPartTitle>
                  <S.HistoryPartDes>{el.currentPoint}</S.HistoryPartDes>
                </S.HistoryPartWrap>
              </S.HistoryLeftWrap>
              {el.description === '포인트 출금' && (
                <S.HistoryRightWrap>
                  <S.ReviewButton
                    type="button"
                    onClick={() => {
                      goToReview(el.sender, el.requestId)
                    }}
                  >
                    후기 작성
                  </S.ReviewButton>
                </S.HistoryRightWrap>
              )}
            </S.HistoryWrap>
          ))
        ) : (
          <S.NoHistoryCard>내역 없음</S.NoHistoryCard>
        )}
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default TradeHistoryPage
