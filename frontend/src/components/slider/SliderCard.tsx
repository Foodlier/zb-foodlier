import React, { useState } from 'react'
import styled from 'styled-components'
import '../../index.css'

interface SliderCardProps {
  title: string
  content: string
  like: boolean
  image: string
}

const BoxContainer = styled.div`
  display: flex;
  justify-content: center;
  flex-direction: column;
  border: 1px solid var(--color-divider);
  border-radius: 1rem;
  box-shadow: 0.1rem 0.5rem 1.1rem var(--color-shadow);

  @media screen and (min-width: 768px) {
    flex-direction: row-reverse;
    justify-content: space-between;
  }
`

const ImgWrap = styled.div`
  width: 30rem;
  height: 30rem;
  display: flex;
  justify-content: center;
  align-items: center;
  width: clamp(100%, 10vw, 30rem);
  height: clamp(15rem, 10vw, 30rem);

  & > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 1rem 1rem 0 0;
  }

  @media screen and (min-width: 768px) {
    width: clamp(40rem, 50vw, 50%);
    height: clamp(30rem, 50vw, 30rem);

    & > img {
      border-radius: 0 1rem 1rem 0;
    }
  }
`

const TextWrap = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 2rem;

  & > div {
    display: flex;
    justify-content: space-between;
  }
  @media screen and (min-width: 768px) {
    padding: 1rem 4rem;
  }
`

const LikeButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
`

const LikeIcon = styled.img`
  width: 2.4rem;
  height: 2.4rem;

  @media screen and (min-width: 768px) {
    width: 3.5rem;
    height: 3.5rem;
  }
`

const Title = styled.h2`
  margin: 0.1rem 0;
  font-size: 2rem;
  font-family: 'Pretendard-Bold';
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  @media screen and (min-width: 768px) {
    font-size: 3rem;
  }
`

const Content = styled.p`
  margin: 1rem 0;
  font-size: 1.4rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  word-wrap: break-word;
  overflow-wrap: break-word;

  @media screen and (min-width: 768px) {
    width: 100%;
    font-size: 2rem;
    max-width: 45rem;
    -webkit-line-clamp: 3;
  }
`

const SliderCard: React.FC<SliderCardProps> = ({
  title,
  content,
  like,
  image,
}) => {
  const [isLiked, setIsLiked] = useState(like)
  const [likeCount, setLikeCount] = useState(like ? 1 : 0)

  const handleLikeClick = () => {
    if (!isLiked) {
      setLikeCount(likeCount + 1)
    } else {
      setLikeCount(likeCount - 1)
    }
    setIsLiked(!isLiked)
  }

  return (
    <BoxContainer>
      <ImgWrap>
        <img src={image} alt={title} />
      </ImgWrap>
      <TextWrap>
        <div>
          <Title>{title}</Title>
          <LikeButton type="button" onClick={handleLikeClick}>
            <LikeIcon
              src={
                isLiked
                  ? '/src/assets/icon/like_fill.svg'
                  : '/src/assets/icon/like_light.svg'
              }
              alt="Like Icon"
            />
            {likeCount}
          </LikeButton>
        </div>
        <Content>{content}</Content>
      </TextWrap>
    </BoxContainer>
  )
}

export default SliderCard
