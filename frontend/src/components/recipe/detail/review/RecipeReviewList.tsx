import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import useIcon from '../../../../hooks/useIcon'
import RecipeReviewItem from './RecipeReviewItem'
import CommonButton from '../../../ui/Button'
import * as S from '../../../../styles/recipe/detail/review/RecipeReviewList.styled'
import { palette } from '../../../../constants/Styles'
import axiosInstance from '../../../../utils/FetchCall'

interface ReviewItem {
  content: string
  cookUrl: string
  createdAt: string
  nickname: string
  profileUrl: string
  recipeId: number
  recipeReviewId: number
  star: number
}

function RecipeReviewList({
  recipeId,
  activeModal,
}: {
  recipeId: number
  activeModal: () => void
}) {
  const navigate = useNavigate()
  const { IcAddLight } = useIcon()
  const [reviewList, setReviewList] = useState<ReviewItem[]>([])
  const [myReview, setMyReview] = useState<ReviewItem>()
  const [pageIdx, setPageIdx] = useState(0)
  const [hasNextReviewPage, setHasNextReviewPage] = useState(true)

  const TOKEN: string | null = JSON.parse(
    localStorage.getItem('accessToken') ?? 'null'
  )

  // 나의 리뷰
  const fetchMyReviewData = async () => {
    const res = await axiosInstance.get(`/api/review/recipe/me/${recipeId}
    `)

    let myReviewData
    if (res.status === 200) {
      myReviewData = res.data
      console.log('내꺼 리뷰 확인 :', myReviewData)
      setMyReview(myReviewData)
    } else {
      console.log('내꺼리뷰 가져오는거실패 ')
    }
  }

  // 리뷰 리스트
  const fetchReviewData = async () => {
    const pageSize = 4

    const res = await axiosInstance.get(
      `/api/review/recipe/${pageIdx}/${pageSize}/${recipeId}`
    )

    if (res.status === 200) {
      const reviewData = res.data.content
      console.log('리뷰 확인 :', res, res.data.hasNextPage)
      setHasNextReviewPage(res.data.hasNextPage)

      if (pageIdx === 0) {
        setReviewList(reviewData)
      } else {
        setReviewList(prevReviewList => [...prevReviewList, ...reviewData])
      }
    }
  }

  useEffect(() => {
    fetchMyReviewData()
    fetchReviewData()
  }, [])

  const handleShowMore = async () => {
    await fetchReviewData()
    setPageIdx(pageIdx + 1)
  }

  return (
    <S.ReviewContainer>
      <S.ReviewtitBox>
        <S.Reviewtit>후기</S.Reviewtit>
        {/* <Link to={`/recipe/detail/write-review/${recipeId}`}> */}
        <CommonButton
          color="main"
          onClick={() => {
            if (TOKEN) {
              navigate(`/recipe/detail/write-review/${recipeId}`)
            } else {
              activeModal()
            }
          }}
        >
          <IcAddLight size={2} color={palette.white} />
          후기 작성하기
        </CommonButton>
        {/* </Link> */}
      </S.ReviewtitBox>
      {/* 나의 리뷰 */}
      {myReview && (
        <RecipeReviewItem key={myReview.recipeReviewId} review={myReview} />
      )}
      {/* 리뷰 리스트 */}
      <S.ReviewUlBox>
        {reviewList.map(review => (
          <RecipeReviewItem key={review.recipeReviewId} review={review} />
        ))}
      </S.ReviewUlBox>

      {/* 더보기 버튼 */}
      <S.MoreButtonBox>
        {reviewList.length > 3 && (
          <CommonButton
            onClick={handleShowMore}
            color={hasNextReviewPage ? 'main' : 'white'}
            border={hasNextReviewPage ? 'border' : 'borderNone'}
          >
            댓글 더보기
          </CommonButton>
        )}
      </S.MoreButtonBox>
    </S.ReviewContainer>
  )
}

export default RecipeReviewList
