/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable no-nested-ternary */
import { useEffect, useState } from 'react'
import { useLocation, useNavigate, useParams } from 'react-router-dom'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/RequestDetailPage.styled'
import axiosInstance from '../../utils/FetchCall'
import { RequestDetail } from '../../constants/Interfaces'
import RecipeItem from '../../components/recipe/RecipeItem'
import EmptyRecipeItem from '../../components/recipe/EmptyRecipeItem'
import ListModal from '../../components/refrigerator/ListModal'

const RequestDetailPage = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const { state } = useLocation()
  const isChat = state?.recipeId || 0
  console.log(state)
  console.log(isChat)

  const [requestValue, setRequestValue] = useState<RequestDetail>()
  const [isQuotationListModal, setIsQuotationListModal] = useState(false)
  const [isSelectedRequestId, setIsSelectedRequestId] = useState(0)
  const [quotationErrorMessage, setQuotationErrorMessage] = useState('')

  const REQUEST_DATA = [
    {
      title: '제목',
      value: requestValue?.title,
    },
    {
      title: '요청자',
      value: requestValue?.requesterNickname,
    },
    {
      title: '요청 가격',
      value: requestValue?.expectedPrice,
    },
    {
      title: '요청 지역',
      value: `${requestValue?.address} ${requestValue?.addressDetail}`,
    },
    {
      title: '요청 시간',
      value: requestValue?.expectedAt,
    },
    {
      title: '요청 내용',
      value: requestValue?.content,
    },
  ]

  // 요청서 상세 조회
  const getRequestDetail = async () => {
    try {
      const { data, status } = await axiosInstance.get(
        `/api/refrigerator/request/${id}`
      )
      console.log(data)
      if (status === 200) {
        setRequestValue(data)
      }
    } catch (error) {
      console.log(error)
    }
  }

  // 요청서 수락
  const approveRequest = () => {
    const res = axiosInstance.post(`/api/refrigerator/chef/approve/${id}`)
    console.log(res)
  }

  // 요청서 거절
  const rejectRequest = () => {
    const res = axiosInstance.patch(`/api/refrigerator/reject/${id}`)
    console.log(res)
  }

  // 견적서 보내기
  const sendQuotation = () => {
    if (!isSelectedRequestId) {
      setQuotationErrorMessage('견적서를 선택해주세요.')
      return
    }

    try {
      const response = axiosInstance.post(
        `/api/quotation/send?quotationId=${isSelectedRequestId}&requestId=${requestValue?.requestId}`
      )
      console.log(response)
      navigate(-1)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getRequestDetail()
  }, [])

  if (!requestValue) return null

  return (
    <>
      <Header />
      <S.RequestContainer>
        <S.RequestHeader>요청서 상세</S.RequestHeader>
        <S.RequestForm action="">
          <S.RequestFormList>
            {REQUEST_DATA.map(item => (
              <S.RequestFormEl key={item.title}>
                <S.ElementTitle>{item.title}</S.ElementTitle>
                <S.ElementContents>{item.value}</S.ElementContents>
              </S.RequestFormEl>
            ))}

            <S.RequestFormEl>
              <S.ElementTitle>보유 재료</S.ElementTitle>
              {requestValue?.ingredientList.map(el => (
                <S.ElementContents key={el}>{el}</S.ElementContents>
              ))}
            </S.RequestFormEl>

            <S.RequestFormEl>
              <S.ElementTitle>태그된 레시피</S.ElementTitle>

              {requestValue?.recipeId ? (
                requestValue?.mainImageUrl ? (
                  <RecipeItem
                    recipeItem={{
                      recipeId: requestValue?.recipeId, // 레시피 ID 필요
                      title: requestValue?.recipeTitle,
                      mainImageUrl: requestValue?.mainImageUrl,
                      content: requestValue?.recipeContent,
                      heartCount: requestValue?.heartCount,
                      isHeart: true,
                    }}
                    onClick={() => console.log('레시피 클릭')}
                  />
                ) : (
                  <div>{requestValue.recipeTitle}</div>
                )
              ) : isSelectedRequestId ? (
                <div>{isSelectedRequestId}</div>
              ) : (
                <S.WrapQuotation>
                  <EmptyRecipeItem
                    onClick={(e: React.MouseEvent<HTMLButtonElement>) => {
                      e.preventDefault()
                      setIsQuotationListModal(true)
                    }}
                  />
                  <S.ErrorMessage>{quotationErrorMessage}</S.ErrorMessage>
                </S.WrapQuotation>
              )}
            </S.RequestFormEl>
          </S.RequestFormList>
          {state && (
            <S.ButtonList>
              <S.RejectButton type="button" onClick={() => navigate(-1)}>
                뒤로가기
              </S.RejectButton>
            </S.ButtonList>
          )}
          {!isChat && !state && (
            <S.ButtonList>
              <S.RejectButton type="button" onClick={rejectRequest}>
                거절하기
              </S.RejectButton>
              {requestValue?.mainImageUrl ? (
                <S.AcceptButton
                  type="button"
                  onClick={approveRequest}
                  $isActive={Boolean(requestValue.mainImageUrl)}
                >
                  수락하기
                </S.AcceptButton>
              ) : (
                <S.AcceptButton
                  type="button"
                  onClick={sendQuotation}
                  $isActive={Boolean(isSelectedRequestId)}
                >
                  견적서 보내기
                </S.AcceptButton>
              )}
            </S.ButtonList>
          )}
        </S.RequestForm>

        <S.SpacingDiv />
      </S.RequestContainer>

      {/* 견적서 목록 Modal */}
      {isQuotationListModal && (
        <ListModal
          setModalOpen={setIsQuotationListModal}
          modalType="quotation"
          setIsSelectedRequestId={setIsSelectedRequestId}
        />
      )}
      <BottomNavigation />
    </>
  )
}

export default RequestDetailPage
