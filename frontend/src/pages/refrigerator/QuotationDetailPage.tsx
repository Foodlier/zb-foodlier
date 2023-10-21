/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useLocation, useNavigate, useParams } from 'react-router-dom'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/QuotationDetailPage.styled'
import axiosInstance from '../../utils/FetchCall'
import { QuotationDetail } from '../../constants/Interfaces'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'

const QuotationDetailPage = () => {
  const navigate = useNavigate()
  const { id } = useParams()
  const { state } = useLocation()
  const { requestId, chef } = state

  const [quotationValue, setQuotationValue] = useState<QuotationDetail>()
  const [isCompleteModal, setIsCompleteModal] = useState(false)

  const getQuotation = async () => {
    const { data } = await axiosInstance.get(`/api/quotation/${id}`)

    setQuotationValue(data)
  }

  const acceptQuotation = async () => {
    const re = await axiosInstance.post(
      `/api/refrigerator/requester/approve/${requestId}`
    )
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
    console.log(re)
  }

  useEffect(() => {
    getQuotation()
  }, [])

  return (
    <>
      <Header />
      <S.RequestContainer>
        <S.RequestHeader>견적서 상세</S.RequestHeader>
        <S.RequestForm action="">
          <S.RequestFormList>
            <S.RequestFormEl>
              <S.ElementTitle>요리사</S.ElementTitle>
              <S.ElementContents>{chef}</S.ElementContents>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>제목</S.ElementTitle>
              <S.ElementContents>{quotationValue?.title}</S.ElementContents>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>내용</S.ElementTitle>
              <S.ElementContents>{quotationValue?.content}</S.ElementContents>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>예상 재료</S.ElementTitle>
              {quotationValue?.recipeIngredientDtoList.map(el => (
                <div key={el.name}>
                  <S.ElementContents>{`${el.name} ${el.count}${el.unit}`}</S.ElementContents>
                </div>
              ))}
            </S.RequestFormEl>
            <S.RequestFormEl>
              <>
                <S.ElementTitle>예상 난이도</S.ElementTitle>
                {quotationValue?.difficulty === 'EASY' && (
                  <S.ElementContents>쉬움</S.ElementContents>
                )}
                {quotationValue?.difficulty === 'MIDEUM' && (
                  <S.ElementContents>보통</S.ElementContents>
                )}
                {quotationValue?.difficulty === 'HARD' && (
                  <S.ElementContents>어려움</S.ElementContents>
                )}
              </>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>예상 조리순서</S.ElementTitle>
              {quotationValue?.recipeDetailDtoList.map((el, index) => (
                <div key={el}>
                  <S.ElementContents>{index + 1}.</S.ElementContents>
                  <S.ElementContents>{el}</S.ElementContents>
                </div>
              ))}
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>예상 조리시간</S.ElementTitle>
              <S.ElementContents>
                {quotationValue?.expectedTime}
              </S.ElementContents>
            </S.RequestFormEl>
          </S.RequestFormList>
          <S.ButtonList>
            <S.RejectButton type="button" onClick={() => navigate(-1)}>
              뒤로가기
            </S.RejectButton>
            <S.AcceptButton type="button" onClick={acceptQuotation}>
              수락하기
            </S.AcceptButton>
          </S.ButtonList>
        </S.RequestForm>
        <S.SpacingDiv />
      </S.RequestContainer>
      {isCompleteModal && (
        <ModalWithoutButton
          content="요청이 수락되었습니다."
          setIsModalFalse={() => setIsCompleteModal(false)}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default QuotationDetailPage
