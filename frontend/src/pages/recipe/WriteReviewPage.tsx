import React, { useState, ChangeEvent, FormEvent, KeyboardEvent } from 'react'
import { Link } from 'react-router-dom'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import * as S from '../../styles/recipe/WriteReviewPage.styled'
import CommonButton from '../../components/ui/Button'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'

function ReviewForm() {
  const { StarFill, StarLight } = useIcon()
  const [rating, setRating] = useState(5)
  const [memo, setMemo] = useState('')
  const [attachment, setAttachment] = useState<File | null>(null)
  const { IcAddRoundDuotone } = useIcon()

  const handleRatingChange = (newRating: number) => {
    if (newRating >= 1 && newRating <= 5) {
      setRating(newRating)
    }
  }

  const handleMemoChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    setMemo(e.target.value)
  }

  const uploadFile = async (file: File) => {
    try {
      const formData = new FormData()
      formData.append('file', file)
      const response = await fetch('your-upload-api-endpoint', {
        method: 'POST',
        body: formData,
      })

      if (response.ok) {
        console.log('파일 업로드 성공')
      } else {
        console.error('파일 업로드 실패')
      }
    } catch (error) {
      console.error('파일 업로드 중 오류 발생', error)
    }
  }

  const handleAttachmentChange = (e: ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files ? e.target.files[0] : null
    if (selectedFile) {
      uploadFile(selectedFile)
      setAttachment(selectedFile)
    }
  }

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault()

    console.log('별점:', rating)
    console.log('후기 내용:', memo)
    console.log('첨부 파일:', attachment)

    setRating(5)
    setMemo('')
    setAttachment(null)
  }

  const handleStarClick = (starValue: number) => {
    handleRatingChange(starValue)
  }

  return (
    <>
      <Header />

      <S.ReviewWriteContainer>
        <S.ReviewWriteTit>레시피 후기 작성하기</S.ReviewWriteTit>
        <S.ReviewWriteForm onSubmit={handleSubmit}>
          <S.ReviewWriteWrap>
            <S.ReviewWriteTxt>사진 업로드</S.ReviewWriteTxt>
            <S.ReviewFileLabel htmlFor="attachment">
              <IcAddRoundDuotone size={4} />
            </S.ReviewFileLabel>
            <S.ReviewWriteInput
              type="file"
              id="attachment"
              onChange={handleAttachmentChange}
            />
          </S.ReviewWriteWrap>

          <S.ReviewWriteWrap>
            <S.ReviewWriteTxt>후기 내용</S.ReviewWriteTxt>
            <S.ReviewWriteLabel hidden htmlFor="memo">
              후기 내용
            </S.ReviewWriteLabel>
            <S.ReviewWriteTextarea
              id="memo"
              value={memo}
              onChange={handleMemoChange}
              rows={4}
              placeholder="후기 내용을 입력해 주세요"
            />
          </S.ReviewWriteWrap>

          <S.ReviewWriteWrap>
            <S.ReviewWriteTxt>레시피 총점</S.ReviewWriteTxt>
            <S.ReviewWriteLabel hidden htmlFor="rating">
              레시피 총점
            </S.ReviewWriteLabel>
            <S.ReviewWriteStar>
              {[1, 2, 3, 4, 5].map(starValue => (
                <S.ReviewStar
                  key={starValue}
                  onClick={() => handleStarClick(starValue)}
                  role="button"
                  tabIndex={0}
                >
                  {starValue <= rating ? (
                    <StarFill size={5} color={palette.yellow} />
                  ) : (
                    <StarLight size={5} color={palette.divider} />
                  )}
                </S.ReviewStar>
              ))}
            </S.ReviewWriteStar>
          </S.ReviewWriteWrap>

          <S.MoreButtonBox>
            <CommonButton color="divider" size="large">
              <Link to="/recipe/detail">취소하기</Link>
            </CommonButton>
            <CommonButton color="main" size="large">
              후기 등록하기
            </CommonButton>
          </S.MoreButtonBox>
        </S.ReviewWriteForm>
      </S.ReviewWriteContainer>

      <BottomNavigation />
    </>
  )
}

export default ReviewForm
