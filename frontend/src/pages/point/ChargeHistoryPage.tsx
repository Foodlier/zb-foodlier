import { useEffect, useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/point/TradeHistoryPage.styled'
import axiosInstance from '../../utils/FetchCall'
import RefundModal from '../../components/point/RefundModal'

interface HistoryType {
  chargeAt: string
  chargePoint: number
  description: string
  paymentKey: string
  canceled: boolean
}

const ChargeHistoryPage = () => {
  const [history, setHistory] = useState<HistoryType[]>([])
  const [isRefundModal, setIsRefundModal] = useState(false)
  const [paymentKey, setPaymentKey] = useState('')

  const getTradeHistory = async () => {
    const pageIdx = 0
    const pageSize = 20
    try {
      const res = await axiosInstance.get(
        `/api/point/charge/${pageIdx}/${pageSize}`
      )
      console.log(res)
      setHistory(res.data.content)
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
        <S.Title>충전내역</S.Title>
        {history.length > 0 ? (
          history.map(el => (
            <S.HistoryWrap key={el.chargeAt}>
              <S.HistoryLeftWrap>
                <S.HistoryPartWrap>
                  <S.HistoryPartTitle>거래 일시</S.HistoryPartTitle>
                  <S.HistoryPartDes>{el.chargeAt}</S.HistoryPartDes>
                </S.HistoryPartWrap>
                <S.HistoryPartWrap>
                  <S.HistoryPartTitle>{el.description}</S.HistoryPartTitle>
                  <S.HistoryPartDes>{el.chargePoint}</S.HistoryPartDes>
                </S.HistoryPartWrap>
              </S.HistoryLeftWrap>
              <S.HistoryRightWrap>
                {el.description === '포인트 충전' &&
                  (el.canceled ? (
                    <S.ReviewButton
                      type="button"
                      onClick={() => {
                        setPaymentKey(el.paymentKey)
                        setIsRefundModal(true)
                      }}
                    >
                      환불하기
                    </S.ReviewButton>
                  ) : (
                    <S.ReviewedButton type="button" disabled>
                      환불완료
                    </S.ReviewedButton>
                  ))}
              </S.HistoryRightWrap>
            </S.HistoryWrap>
          ))
        ) : (
          <S.NoHistoryCard>내역 없음</S.NoHistoryCard>
        )}
      </S.Container>
      <BottomNavigation />
      {isRefundModal && (
        <RefundModal
          setIsRefundModal={setIsRefundModal}
          paymentKey={paymentKey}
        />
      )}
    </>
  )
}

export default ChargeHistoryPage
